import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import api from "../../api/api";

export const get_revennue_stats = createAsyncThunk(
  "stat/get_revennue_stats",
  async (option, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(
        `/superadmin/revenueTotalOfYear?year=${option.year}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      console.log(data);
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const get_category_revenue = createAsyncThunk(
  "stat/get_category_revenue",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(
        `/superadmin/revenueOfCategory?startDate=${info.startDate}&endDate=${info.endDate}&categoryId=${info.id}`
      );
      console.log(data);
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const get_product_revenue = createAsyncThunk(
  "stat/get_product_revenue",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(
        `/superadmin/revenueOfProduct?startDate=${info.startDate}&endDate=${info.endDate}&productId=${info.id}`
      );
      console.log(data);
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const get_store_revenue = createAsyncThunk(
  "stat/get_store_revenue",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(
        `/superadmin/revenueOfStore?startDate=${info.startDate}&endDate=${info.endDate}`
      );
      console.log(data);
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const get_categories_revenue = createAsyncThunk(
  "stat/get_categories_revenue",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(
        `/superadmin/revenueStatisticsForAllCategories?startDate=${info.startDate}&endDate=${info.endDate}`
      );
      console.log(data);
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const get_products_revenue = createAsyncThunk(
  "stat/get_products_revenue",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(
        `/superadmin/revenueStatisticsForAllProducts?startDate=${info.startDate}&endDate=${info.endDate}`
      );
      console.log(data);
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

const statSlice = createSlice({
  name: "stat",
  initialState: {
    totalSales: 0,
    totalOrders: 0,
    totalUsers: 0,
    totalProducts: 0,
    chart: { labels: [], data: [] },
    table: [],
    loader: false,
    success: false,
    errorMessage: "",
  },
  reducers: {
    clearMessage: (state) => {
      state.errorMessage = "";
      state.success = false;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(get_revennue_stats.pending, (state) => {
      state.loader = true;
    });
    builder.addCase(get_revennue_stats.fulfilled, (state, { payload }) => {
      state.loader = false;
      state.success = true;
      state.totalUsers = payload.totalUsers;
      state.totalOrders = payload.totalOrders;
      state.totalSales = payload.totalSales;
      state.totalProducts = payload.totalProducts;
      state.chart = payload.chartData;
      state.table = payload.tableData;
    });
    builder.addCase(get_revennue_stats.rejected, (state, { payload }) => {
      state.loader = false;
      state.errorMessage = payload;
    });
  },
});

export const { clearMessage } = statSlice.actions;
export default statSlice.reducer;
