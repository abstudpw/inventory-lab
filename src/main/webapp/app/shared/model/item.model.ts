import { IEquipment } from 'app/shared/model/equipment.model';
import {IRental} from "app/shared/model/rental.model";

export interface IItem {
  id?: number;
  rented?: boolean | null;
  equipment?: IEquipment;
  rental?: IRental;
}

export const defaultValue: Readonly<IItem> = {
  rented: false,
};
