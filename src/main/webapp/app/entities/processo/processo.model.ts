import dayjs from 'dayjs/esm';

export interface IProcesso {
  id: number;
  npu?: string;
  dataCadastro?: dayjs.Dayjs | null;
  dataVisualizacao?: dayjs.Dayjs | null;
  municipio?: string ;
  uf?: string;
  documento?: string;
}

export interface IBGEEstado {
  id: number; 
  nome: string;
  municipio: {
    id: number;
    nome: string;  
  };
}

export interface IBGEMunicipio {
  id: number; 
  nome: string;
}

export type NewProcesso = Omit<IProcesso, 'id'> & { id: null };
