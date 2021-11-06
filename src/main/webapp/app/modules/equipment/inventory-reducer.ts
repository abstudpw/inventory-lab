import {createAsyncThunk, isFulfilled, isPending} from "@reduxjs/toolkit";
import {createEntitySlice, EntityState, IQueryParams, serializeAxiosError} from "app/shared/reducers/reducer.utils";
import axios from "axios";
import {defaultValue} from "app/shared/model/inventory";
import {IInventory} from "app/shared/model/inventory";

const initialState: EntityState<IInventory> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/inventory';

// Actions

export const getEntities = createAsyncThunk('inventory', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}`;
  return axios.get<IInventory[]>(requestUrl);
});


export const InventorySlice = createEntitySlice({
  name: 'inventory',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        return {
          ...state,
          loading: false,
          entities: action.payload.data,
        };
      })
  },
});

export const { reset } = InventorySlice.actions;

// Reducer
export default InventorySlice.reducer;

