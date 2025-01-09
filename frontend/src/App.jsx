import { useEffect, useState } from "react";
import Router from "./router/Router";
import publicRoutes from "./router/routes/publicRoutes";
import { getRoute } from "./router/routes/index";
import { get_user_info, log_out } from "./store/Reducers/authReducer";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";

function App() {
  const [allRoutes, setAllRoutes] = useState([...publicRoutes]);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  console.log("App.js");

  useEffect(() => {
    const route = getRoute();
    setAllRoutes([...allRoutes, route]);
  }, []);

  return <Router allRoutes={allRoutes} />;
}

export default App;
