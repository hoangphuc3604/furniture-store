import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import api from "../../api/api";

export const get_users = createAsyncThunk(
  "users/get_users",
  async (_, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get("/user/admin/getAllNormalUser", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      });
      console.log(data);
      return fulfillWithValue(data);
    } catch (error) {
      console.log(error.response);
      return rejectWithValue(error.response.data);
    }
  }
);

export const get_admins = createAsyncThunk(
  "users/get_admins",
  async (_, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get("/user/superAdmin/getAllAdmin");
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const get_user_by_id = createAsyncThunk(
  "users/get_user_by_id",
  async (id, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(`/user/admin/getById?id=${id}`, {
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

export const update_user = createAsyncThunk(
  "users/update_user",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.post("/user/updateUser", info, {
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

export const lock_user = createAsyncThunk(
  "users/lock_user",
  async (id, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(
        `/user/admin/deactivateUserWithId?id=${id}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const active_user = createAsyncThunk(
  "users/active_user",
  async (id, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(`/user/admin/activeUserWithId?id=${id}`, {
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

const usersSlice = createSlice({
  name: "users",
  initialState: {
    users: [],
    user: {},
    total: 0,
    success: false,
    errorMessage: "",
    loader: false,
  },
  reducers: {
    clearMessage: (state) => {
      state.success = false;
      state.errorMessage = "";
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(get_users.pending, (state) => {
        state.loader = true;
      })
      .addCase(get_users.fulfilled, (state, { payload }) => {
        state.users = payload;
        state.total = payload.length;
        state.loader = false;
      })
      .addCase(get_users.rejected, (state, { payload }) => {
        state.errorMessage = payload;
        state.loader = false;
      })
      .addCase(get_user_by_id.pending, (state) => {
        state.loader = true;
      })
      .addCase(get_user_by_id.fulfilled, (state, { payload }) => {
        state.user = payload;
        state.loader = false;
      })
      .addCase(get_user_by_id.rejected, (state, { payload }) => {
        state.errorMessage = payload;
        state.loader = false;
      })
      .addCase(update_user.pending, (state) => {
        state.loader = true;
      })
      .addCase(update_user.fulfilled, (state) => {
        state.success = true;
        state.loader = false;
      })
      .addCase(update_user.rejected, (state, { payload }) => {
        state.errorMessage = payload;
        state.loader = false;
      })
      .addCase(lock_user.pending, (state) => {
        state.loader = true;
      })
      .addCase(lock_user.fulfilled, (state) => {
        state.success = true;
        state.loader = false;
      })
      .addCase(lock_user.rejected, (state, { payload }) => {
        state.errorMessage = payload;
        state.loader = false;
      })
      .addCase(active_user.pending, (state) => {
        state.loader = true;
      })
      .addCase(active_user.fulfilled, (state) => {
        state.success = true;
        state.loader = false;
      })
      .addCase(active_user.rejected, (state, { payload }) => {
        state.errorMessage = payload;
        state.loader = false;
      })
      .addCase(get_admins.pending, (state) => {
        state.loader = true;
      })
      .addCase(get_admins.fulfilled, (state, { payload }) => {
        state.users = payload;
        state.total = payload.length;
        state.loader = false;
      })
      .addCase(get_admins.rejected, (state, { payload }) => {
        state.errorMessage = payload;
        state.loader = false;
      });
  },
});

export const { clearMessage } = usersSlice.actions;
export default usersSlice.reducer;
