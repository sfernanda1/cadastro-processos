import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ProcessoService } from '../service/processo.service';
import { IProcesso } from '../processo.model';
import { ProcessoFormService } from './processo-form.service';

import { ProcessoUpdateComponent } from './processo-update.component';

describe('Processo Management Update Component', () => {
  let comp: ProcessoUpdateComponent;
  let fixture: ComponentFixture<ProcessoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let processoFormService: ProcessoFormService;
  let processoService: ProcessoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProcessoUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProcessoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProcessoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    processoFormService = TestBed.inject(ProcessoFormService);
    processoService = TestBed.inject(ProcessoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const processo: IProcesso = { id: 25438 };

      activatedRoute.data = of({ processo });
      comp.ngOnInit();

      expect(comp.processo).toEqual(processo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcesso>>();
      const processo = { id: 8802 };
      jest.spyOn(processoFormService, 'getProcesso').mockReturnValue(processo);
      jest.spyOn(processoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ processo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: processo }));
      saveSubject.complete();

      // THEN
      expect(processoFormService.getProcesso).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(processoService.update).toHaveBeenCalledWith(expect.objectContaining(processo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcesso>>();
      const processo = { id: 8802 };
      jest.spyOn(processoFormService, 'getProcesso').mockReturnValue({ id: null });
      jest.spyOn(processoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ processo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: processo }));
      saveSubject.complete();

      // THEN
      expect(processoFormService.getProcesso).toHaveBeenCalled();
      expect(processoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProcesso>>();
      const processo = { id: 8802 };
      jest.spyOn(processoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ processo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(processoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
