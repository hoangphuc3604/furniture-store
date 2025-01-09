import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import api from "../../api/api";

export const get_user_info = createAsyncThunk(
  "auth/get_user_info",
  async (_, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get("/user/getCurrentUser", {
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

export const user_login = createAsyncThunk(
  "auth/user_login",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.post("/auth/login", info);
      localStorage.setItem("accessToken", data.access_token);
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const user_register = createAsyncThunk(
  "auth/user_register",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.post("/auth/register", info);
      return fulfillWithValue(data);
    } catch (error) {
      console.log(error);
      return rejectWithValue(error.response.data);
    }
  }
);

export const user_forgot_password = createAsyncThunk(
  "auth/user_forgot_password",
  async (email, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(`/auth/forgotPassword?email=${email}`);
      return fulfillWithValue(data);
    } catch (error) {
      console.log(error);
      return rejectWithValue(error.response.data);
    }
  }
);

export const user_verification = createAsyncThunk(
  "auth/user_verification",
  async (email, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(`/auth/verification?email=${email}`);
      return fulfillWithValue(data);
    } catch (error) {
      console.log(error);
      return rejectWithValue(error.response.data);
    }
  }
);

export const user_OTP_validation = createAsyncThunk(
  "auth/user_OTP_validation",
  async (email, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(`/auth/validateOTP?email=${email}`);
      return fulfillWithValue(data);
    } catch (error) {
      console.log(error);
      return rejectWithValue(error.response.data);
    }
  }
);

export const authReducer = createSlice({
  name: "auth",
  initialState: {
    success: false,
    errorMessage: "",
    loader: false,
    userInfo: {},
    isLogged: false,
  },
  reducers: {
    messageClear: (state, _) => {
      state.errorMessage = "";
      state.success = "";
    },
  },
  extraReducers: (builder) => {
    builder.addCase(get_user_info.pending, (state, _) => {
      state.loader = true;
    });
    builder.addCase(get_user_info.fulfilled, (state, { payload }) => {
      state.loader = false;
      state.userInfo = payload;
      state.isLogged = true;
    });
    builder.addCase(get_user_info.rejected, (state, { payload }) => {
      state.loader = false;
      state.errorMessage = payload;
    });
    builder.addCase(user_login.pending, (state, _) => {
      state.loader = true;
    });
    builder.addCase(user_login.fulfilled, (state, { payload }) => {
      state.loader = false;
      state.success = true;
      state.isLogged = true;
    });
    builder.addCase(user_login.rejected, (state, { payload }) => {
      state.loader = false;
      state.errorMessage = payload;
    });

    builder.addCase(user_register.pending, (state, _) => {
      state.loader = true;
    });
    builder.addCase(user_register.fulfilled, (state, { payload }) => {
      state.loader = false;
      state.success = true;
    });
    builder.addCase(user_register.rejected, (state, { payload }) => {
      state.loader = false;
      state.errorMessage = payload;
    });

    builder.addCase(user_forgot_password.pending, (state, _) => {
      state.loader = true;
    });
    builder.addCase(user_forgot_password.fulfilled, (state, { payload }) => {
      state.loader = false;
      state.success = true;
    });
    builder.addCase(user_forgot_password.rejected, (state, { payload }) => {
      state.loader = false;
      state.errorMessage = payload;
    });
  },
});

export const { messageClear } = authReducer.actions;
export default authReducer.reducer;
