import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProcesso, NewProcesso } from '../processo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProcesso for edit and NewProcessoFormGroupInput for create.
 */
type ProcessoFormGroupInput = IProcesso | PartialWithRequiredKeyOf<NewProcesso>;

type ProcessoFormDefaults = Pick<NewProcesso, 'id'>;

type ProcessoFormGroupContent = {
  id: FormControl<IProcesso['id'] | NewProcesso['id']>;
  npu: FormControl<IProcesso['npu']>;
  dataCadastro: FormControl<IProcesso['dataCadastro']>;
  dataVisualizacao: FormControl<IProcesso['dataVisualizacao']>;
  municipio: FormControl<IProcesso['municipio']>;
  uf: FormControl<IProcesso['uf']>;
  documento: FormControl<IProcesso['documento']>;
};

export type ProcessoFormGroup = FormGroup<ProcessoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProcessoFormService {
  createProcessoFormGroup(processo: ProcessoFormGroupInput = { id: null }): ProcessoFormGroup {
    const processoRawValue = {
      ...this.getFormDefaults(),
      ...processo,
    };
    return new FormGroup<ProcessoFormGroupContent>({
      id: new FormControl(
        { value: processoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      npu: new FormControl(processoRawValue.npu, {
        nonNullable: true,
        validators: [Validators.required],
      }),
      dataCadastro: new FormControl(processoRawValue.dataCadastro),
      dataVisualizacao: new FormControl(processoRawValue.dataVisualizacao),
      municipio: new FormControl(processoRawValue.municipio, {
        nonNullable: true,
        validators: [Validators.required],
      }),
      uf: new FormControl(processoRawValue.uf, {
        nonNullable: true,
        validators: [Validators.required],
      }),
      documento: new FormControl(processoRawValue.documento, {
        nonNullable: true,
        validators: [Validators.required],
      }),
    });
  }
  getProcesso(form: ProcessoFormGroup): IProcesso | NewProcesso {
    return form.getRawValue() as IProcesso | NewProcesso;
  }

  resetForm(form: ProcessoFormGroup, processo: ProcessoFormGroupInput): void {
    const processoRawValue = { ...this.getFormDefaults(), ...processo };
    form.reset(
      {
        ...processoRawValue,
        id: { value: processoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProcessoFormDefaults {
    return {
      id: null,
    };
  }
}
