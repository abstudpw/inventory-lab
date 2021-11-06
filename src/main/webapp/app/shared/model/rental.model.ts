import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IItem } from 'app/shared/model/item.model';

export interface IRental {
  id?: number;
  from?: string | null;
  to?: string | null;
  active?: boolean | null;
  rentedBy?: IUser;
  rentedItem?: IItem;
}

export const defaultValue: Readonly<IRental> = {
  active: false,
};
