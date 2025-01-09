import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import api from "../../api/api";

export const get_products = createAsyncThunk(
  "products/get_products",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(
        `/products?page=${info.currentPage - 1}&limit=${info.perPage}`
      );
      console.log(data);
      return fulfillWithValue(data);
    } catch (error) {
      console.log(error.response.data);
      return rejectWithValue(error.response.data);
    }
  }
);

export const get_category = createAsyncThunk(
  "products/get_category",
  async (id, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(`/products?category_id=${id}`);
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const product_search = createAsyncThunk(
  "products/search",
  async (keyword, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(`/products?keyword=${keyword}`);
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const product_filter = createAsyncThunk(
  "products/filter",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(
        `/products?page=${info.page}&limit=${info.limit}&keyword=${info.keyword}&category_id=${info.category_id}&material_id=${info.material_id}`
      );
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const get_product = createAsyncThunk(
  "products/get_product",
  async (id, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(`/products/${id}`);
      console.log(data);
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const get_related_products = createAsyncThunk(
  "products/get_related_products",
  async (id, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get(`/products/${id}/related`);
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const add_product = createAsyncThunk(
  "products/add_product",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.post("/products", info, {
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

export const update_product = createAsyncThunk(
  "products/update_product",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.post(`/products`, info, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
      });
      return fulfillWithValue(data);
    } catch (error) {
      console.log(error.response.data);
      return rejectWithValue(error.response.data);
    }
  }
);

export const delete_product = createAsyncThunk(
  "products/delete_product",
  async (id, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.delete(`/products/${id}`, {
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

export const add_product_image = createAsyncThunk(
  "medias/add_product_image",
  async (info, { fulfillWithValue, rejectWithValue }) => {
    try {
      console.log(info);
      const { data } = await api.post(
        `/medias?productId=${info.product_id}`,
        info.formData,
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

export const delete_product_image = createAsyncThunk(
  "products/delete_product_image",
  async (id, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.delete(`/medias/${id}`, {
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

export const get_featured_products = createAsyncThunk(
  "products/get_featured_products",
  async (_, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get("/featured_products");
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const get_new_arrivals = createAsyncThunk(
  "products/get_new_arrivals",
  async (_, { fulfillWithValue, rejectWithValue }) => {
    try {
      const { data } = await api.get("/products_new");
      return fulfillWithValue(data);
    } catch (error) {
      return rejectWithValue(error.response.data);
    }
  }
);

export const productSlice = createSlice({
  name: "products",
  initialState: {
    products: [],
    product: {},
    relatedProducts: [],
    featuredProducts: [],
    newArrivals: [],
    totalPage: 0,
    success: false,
    errorMessage: "",
    loader: false,
  },
  reducers: {
    clearMessage: (state) => {
      state.errorMessage = "";
      state.success = false;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(get_products.pending, (state) => {
        state.loader = true;
      })
      .addCase(get_products.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.products = payload.products;
        state.totalPage = payload.totalPages;
      })
      .addCase(get_products.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(get_product.pending, (state) => {
        state.loader = true;
      })
      .addCase(get_product.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.product = payload;
      })
      .addCase(get_product.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(get_related_products.pending, (state) => {
        state.loader = true;
      })
      .addCase(get_related_products.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.relatedProducts = payload;
        state.success = true;
      })
      .addCase(get_related_products.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(add_product.pending, (state) => {
        state.loader = true;
      })
      .addCase(add_product.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.product = payload;
        state.success = true;
      })
      .addCase(add_product.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(update_product.pending, (state) => {
        state.loader = true;
      })
      .addCase(update_product.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.success = true;
      })
      .addCase(update_product.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(delete_product.pending, (state) => {
        state.loader = true;
      })
      .addCase(delete_product.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.success = true;
      })
      .addCase(delete_product.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(get_featured_products.pending, (state) => {
        state.loader = true;
      })
      .addCase(get_featured_products.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.featuredProducts = payload;
        state.success = true;
      })
      .addCase(get_featured_products.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(get_new_arrivals.pending, (state) => {
        state.loader = true;
      })
      .addCase(get_new_arrivals.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.newArrivals = payload;
        state.success = true;
      })
      .addCase(get_new_arrivals.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(add_product_image.pending, (state) => {
        state.loader = true;
      })
      .addCase(add_product_image.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.success = true;
      })
      .addCase(add_product_image.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      })
      .addCase(delete_product_image.pending, (state) => {
        state.loader = true;
      })
      .addCase(delete_product_image.fulfilled, (state, { payload }) => {
        state.loader = false;
        state.success = true;
      })
      .addCase(delete_product_image.rejected, (state, { payload }) => {
        state.loader = false;
        state.errorMessage = payload;
      });
  },
});

export const { clearMessage } = productSlice.actions;
export default productSlice.reducer;
