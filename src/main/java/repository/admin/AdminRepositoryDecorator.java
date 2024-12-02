package repository.admin;

import model.User;

public abstract class AdminRepositoryDecorator implements AdminRepository{
  protected AdminRepository decoratedAdminRepository;
  public AdminRepositoryDecorator(AdminRepository adminRepository){
      decoratedAdminRepository = adminRepository;
  }
}
