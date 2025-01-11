import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import api from "../../api/api";

// Đổi tên các trường trong object trả về từ API theo API từ backend
export const get_categories = createAsyncThunk(
  "category/get_categories",
  async (pageData, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(`/categories`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      });
      console.log(data);
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const get_category = createAsyncThunk(
  "category/get_category",
  async (id, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(`/category${id}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      });
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const add_category = createAsyncThunk(
  "category/add_category",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.post("/category", info, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      });
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const delete_category = createAsyncThunk(
  "category/delete_category",
  async (id, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.delete(`/category/${id}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      });
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const update_category = createAsyncThunk(
  "category/update_category",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.put("/category", info, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      });
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const categorySlice = createSlice({
  name: "category",
  initialState: {
    categories: [],
    category: {},
    success: false,
    errorMessage: "",
    loader: false,
    total: 0,
  },
  reducers: {
    clearMessage: (state) => {
      state.successMessage = "";
      state.errorMessage = "";
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(get_categories.pending, (state) => {
        state.loader = true;
      })
      .addCase(get_categories.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.categories = payload;
        state.total = payload.length;
        state.success = true;
      })
      .addCase(get_categories.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(get_category.pending, (state) => {
        state.loader = true;
      })
      .addCase(get_category.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.category = payload;
        state.success = true;
      })
      .addCase(get_category.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(add_category.pending, (state) => {
        state.loader = true;
      })
      .addCase(add_category.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.success = true;
        state.successMessage = payload.message;
      })
      .addCase(add_category.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(delete_category.pending, (state) => {
        state.loader = true;
      })
      .addCase(delete_category.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.success = true;
        state.successMessage = payload.message;
      })
      .addCase(delete_category.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(update_category.pending, (state) => {
        state.loader = true;
      })
      .addCase(update_category.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.success = true;
        state.successMessage = payload.message;
      })
      .addCase(update_category.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      });
  },
});

export const { clearMessage } = categorySlice.actions;
export default categorySlice.reducer;
