import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ProcessoService } from '../service/processo.service';
import { IBGEMunicipio, IBGEEstado, IProcesso } from '../processo.model';
import { ProcessoFormGroup, ProcessoFormService } from './processo-form.service';
import dayjs from 'dayjs/esm';


@Component({
  selector: 'jhi-processo-update',
  templateUrl: './processo-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProcessoUpdateComponent implements OnInit {
  isSaving = false;
  processo: IProcesso | null = null;
  states: IBGEEstado[] = [];
  municipalities: IBGEMunicipio[] = [];
  isEdit = false;
  fileName: string | null = null;

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected processoService = inject(ProcessoService);
  protected processoFormService = inject(ProcessoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  editForm: ProcessoFormGroup = this.processoFormService.createProcessoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ processo }) => {
      this.processo = processo;
      if (processo) {
        this.isEdit = true;
        this.updateForm(processo);
      } else {
        this.editForm.get('municipio')?.disable();
      }
    });

    this.loadStates();
  }

  private loadStates(): void {
    this.processoService.getEstados().subscribe(response => {
      this.states = response.body ?? [];
      const ufValue: string | undefined = this.editForm.get('uf')?.value;
      if (this.isEdit && ufValue) {
        this.loadMunicipalities(ufValue);
      }
    });
  }

  private loadMunicipalities(ufName: string): void {
    const selectedState = this.states.find(state => state.nome === ufName);
    if (selectedState) {
      this.processoService.getMunicipios(selectedState.id).subscribe({
        next: response => {
          this.municipalities = response.body ?? [];
          this.editForm.get('municipio')?.enable();
        },
        error: err => {
          console.error('Erro ao carregar municípios:', err);
          this.municipalities = [];
          this.editForm.get('municipio')?.disable();
        }
      });
    } else {
      this.municipalities = [];
      this.editForm.get('municipio')?.disable();
    }
  }

  onUfChangeEvent(event: Event): void {
    const target = event.target as HTMLSelectElement;
    this.onUfChange(target.value);
  }

  onUfChange(selectedUfName: string): void {
    if (!selectedUfName) {
      this.editForm.patchValue({ uf: '' });
      this.municipalities = [];
      this.editForm.get('municipio')?.disable();
      return;
    }
    this.editForm.patchValue({ municipio: undefined });
    this.editForm.patchValue({ uf: selectedUfName });
    this.loadMunicipalities(selectedUfName);
  }
  
  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }


  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      next: fileContent => {
        const input = event.target as HTMLInputElement;
        if (input.files && input.files.length > 0) {
          this.fileName = input.files[0].name;
        }
      },
      error: (err: FileLoadError) => {
        this.eventManager.broadcast(new EventWithContent<AlertError>('desafioTecnicoApp.error', { message: err.message }));
      }
    });
  }

  removeFile(): void {
    this.fileName = null;
    this.editForm.patchValue({ documento: undefined });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    if (!this.isEdit) {
      this.editForm.patchValue({ dataCadastro: dayjs(new Date()) });
    }
    const processo = this.processoFormService.getProcesso(this.editForm);
    if (processo.id !== null) {
      this.subscribeToSaveResponse(this.processoService.update(processo));
    } else {
      this.subscribeToSaveResponse(this.processoService.create(processo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProcesso>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(processo: IProcesso): void {
    this.processo = processo;
    this.processoFormService.resetForm(this.editForm, processo);
  }
}
