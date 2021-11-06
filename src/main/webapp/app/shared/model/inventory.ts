import {IEquipment} from "app/shared/model/equipment.model";
import {IItem} from "app/shared/model/item.model";
import {ITag} from "app/shared/model/tag.model";

export interface IInventory {

  equipment?: IEquipment;
  items?: IItem[];
  tags?: ITag[];
}

export const defaultValue: Readonly<IInventory> = {}
