import AccessEnum from "@/access/accessEnum";

const checkAccess = (loginUser: any, needAccess = AccessEnum.NOT_LOGIN) => {
  const loginUserAccess = loginUser.userRole ?? AccessEnum.NOT_LOGIN;
  if (needAccess === AccessEnum.NOT_LOGIN) {
    return true;
  }
  if (needAccess === AccessEnum.USER) {
    if (loginUserAccess == AccessEnum.NOT_LOGIN) {
      return false;
    }
  }
  if (needAccess === AccessEnum.ADMIN) {
    // 没有管理员权限，返回false
    if (loginUserAccess !== AccessEnum.ADMIN) {
      return false;
    }
  }
  return true;
};

export default checkAccess;
