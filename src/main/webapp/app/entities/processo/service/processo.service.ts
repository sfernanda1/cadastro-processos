import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBGEEstado, IBGEMunicipio, IProcesso, NewProcesso } from '../processo.model';

export type PartialUpdateProcesso = Partial<IProcesso> & Pick<IProcesso, 'id'>;

type RestOf<T extends IProcesso | NewProcesso> = Omit<T, 'dataCadastro' | 'dataVisualizacao'> & {
  dataCadastro?: string | null;
  dataVisualizacao?: string | null;
};

export type RestProcesso = RestOf<IProcesso>;

export type NewRestProcesso = RestOf<NewProcesso>;

export type PartialUpdateRestProcesso = RestOf<PartialUpdateProcesso>;

export type EntityResponseType = HttpResponse<IProcesso>;
export type EntityArrayResponseType = HttpResponse<IProcesso[]>;

@Injectable({ providedIn: 'root' })
export class ProcessoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/processos');
  protected IBGEUrl = this.applicationConfigService.getEndpointFor('/api/ibge-bridge');

  create(processo: NewProcesso): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(processo);
    return this.http
      .post<RestProcesso>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(processo: IProcesso): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(processo);
    return this.http
      .put<RestProcesso>(`${this.resourceUrl}/${this.getProcessoIdentifier(processo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(processo: PartialUpdateProcesso): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(processo);
    return this.http
      .patch<RestProcesso>(`${this.resourceUrl}/${this.getProcessoIdentifier(processo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProcesso>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProcesso[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProcessoIdentifier(processo: Pick<IProcesso, 'id'>): number {
    return processo.id;
  }

  compareProcesso(o1: Pick<IProcesso, 'id'> | null, o2: Pick<IProcesso, 'id'> | null): boolean {
    return o1 && o2 ? this.getProcessoIdentifier(o1) === this.getProcessoIdentifier(o2) : o1 === o2;
  }

  addProcessoToCollectionIfMissing<Type extends Pick<IProcesso, 'id'>>(
    processoCollection: Type[],
    ...processosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const processos: Type[] = processosToCheck.filter(isPresent);
    if (processos.length > 0) {
      const processoCollectionIdentifiers = processoCollection.map(processoItem => this.getProcessoIdentifier(processoItem));
      const processosToAdd = processos.filter(processoItem => {
        const processoIdentifier = this.getProcessoIdentifier(processoItem);
        if (processoCollectionIdentifiers.includes(processoIdentifier)) {
          return false;
        }
        processoCollectionIdentifiers.push(processoIdentifier);
        return true;
      });
      return [...processosToAdd, ...processoCollection];
    }
    return processoCollection;
  }

  protected convertDateFromClient<T extends IProcesso | NewProcesso | PartialUpdateProcesso>(processo: T): RestOf<T> {
    return {
      ...processo,
      dataCadastro: processo.dataCadastro?.format(DATE_FORMAT) ?? null,
      dataVisualizacao: processo.dataVisualizacao?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restProcesso: RestProcesso): IProcesso {
    return {
      ...restProcesso,
      dataCadastro: restProcesso.dataCadastro ? dayjs(restProcesso.dataCadastro) : undefined,
      dataVisualizacao: restProcesso.dataVisualizacao ? dayjs(restProcesso.dataVisualizacao) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProcesso>): HttpResponse<IProcesso> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProcesso[]>): HttpResponse<IProcesso[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  getEstados(): Observable<HttpResponse<IBGEEstado[]>> {
    return this.http.get<IBGEEstado[]>(`${this.IBGEUrl}/estados`, { observe: 'response' });
  }
  
  getMunicipios(ufId: number): Observable<HttpResponse<IBGEMunicipio[]>> {
    return this.http.get<IBGEMunicipio[]>(`${this.IBGEUrl}/estados/${ufId}/municipios`, { observe: 'response' });
  }
}
