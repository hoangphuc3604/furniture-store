import axios from "axios";

const api = axios.create({
  baseURL: "https://furniturestore-zoqo.onrender.com",
});

export default api;
