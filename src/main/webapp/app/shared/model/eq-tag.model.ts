import { IEquipment } from 'app/shared/model/equipment.model';
import { ITag } from 'app/shared/model/tag.model';

export interface IEqTag {
  id?: number;
  equipment?: IEquipment;
  tag?: ITag;
}

export const defaultValue: Readonly<IEqTag> = {};
