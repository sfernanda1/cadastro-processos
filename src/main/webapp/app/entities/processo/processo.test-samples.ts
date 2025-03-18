import dayjs from 'dayjs/esm';

import { IProcesso, NewProcesso } from './processo.model';

export const sampleWithRequiredData: IProcesso = {
  id: 22101,
};

export const sampleWithPartialData: IProcesso = {
  id: 17204,
  dataVisualizacao: dayjs('2025-03-15'),
  uf: 'fax uh-huh',
};

export const sampleWithFullData: IProcesso = {
  id: 21819,
  npu: 'fooey married',
  dataCadastro: dayjs('2025-03-15'),
  dataVisualizacao: dayjs('2025-03-15'),
  municipio: 'swing',
  uf: 'baritone instantly decide',
  documento: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewProcesso = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
