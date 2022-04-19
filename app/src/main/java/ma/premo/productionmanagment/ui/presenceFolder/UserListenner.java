package ma.premo.productionmanagment.ui.presenceFolder;


import java.util.List;

import ma.premo.productionmanagment.models.User;

public interface UserListenner {

  public void getUsersSelected(List<User> list);
}
