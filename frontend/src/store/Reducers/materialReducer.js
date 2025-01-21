import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import api from "../../api/api";

export const get_materials = createAsyncThunk(
  "materials/get_materials",
  async (_, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get("/materials", {
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

export const add_material = createAsyncThunk(
  "materials/add_material",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.post("/materials", info, {
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

export const update_material = createAsyncThunk(
  "materials/update_material",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.put(`/materials/${info.id}`, info, {
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

export const delete_material = createAsyncThunk(
  "materials/delete_material",
  async (id, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.delete(`/materials/${id}`, {
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

export const get_colors = createAsyncThunk(
  "materials/get_colors",
  async (_, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get("/colors");
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

const materialSlice = createSlice({
  name: "materials",
  initialState: {
    loader: false,
    success: false,
    errorMessage: "",
    materials: [],
    colors: [],
  },
  reducers: {
    clearMessage: (state) => {
      state.success = false;
      state.errorMessage = "";
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(get_materials.pending, (state) => {
        state.loader = true;
      })
      .addCase(get_materials.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.materials = payload;
      })
      .addCase(get_materials.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(add_material.pending, (state) => {
        state.loader = true;
      })
      .addCase(add_material.fulfilled, (state) => {
        state.loader = false;
        state.success = true;
      })
      .addCase(add_material.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(update_material.pending, (state) => {
        state.loader = true;
      })
      .addCase(update_material.fulfilled, (state) => {
        state.loader = false;
        state.success = true;
      })
      .addCase(update_material.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(delete_material.pending, (state) => {
        state.loader = true;
      })
      .addCase(delete_material.fulfilled, (state) => {
        state.loader = false;
        state.success = true;
      })
      .addCase(delete_material.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(get_colors.pending, (state) => {
        state.loader = true;
      })
      .addCase(get_colors.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.colors = payload;
      })
      .addCase(get_colors.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      });
  },
});

export const { clearMessage } = materialSlice.actions;
export default materialSlice.reducer;
