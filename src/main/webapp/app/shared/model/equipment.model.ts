import { IProducer } from 'app/shared/model/producer.model';

export interface IEquipment {
  id?: number;
  name?: string;
  model?: string;
  photo?: string | null;
  description?: string | null;
  url?: string | null;
  producer?: IProducer;
}

export const defaultValue: Readonly<IEquipment> = {};
