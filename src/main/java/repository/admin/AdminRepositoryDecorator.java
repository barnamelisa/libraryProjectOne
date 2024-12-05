package repository.admin;

import model.User;

public abstract class AdminRepositoryDecorator implements AdminRepository{

    // trebuie sa fie protected pt ca astfel clasele care mostenesc AdminRepositoryDecorator sa aiba acces la acest atribut
    protected AdminRepository decoratedAdminRepository;
  public AdminRepositoryDecorator(AdminRepository adminRepository){
      decoratedAdminRepository = adminRepository;
  }
}
