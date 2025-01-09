import { allNav } from "./allNav";

export const getNav = (role) => {
  return allNav.filter((nav) => nav.role.includes(role));
};
