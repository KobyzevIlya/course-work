class ApplicationController < ActionController::Base
  def authenticate_user!
    redirect_to :login if session[:login].nil?
  end

  def check_authorize!
    raise NotAuthorizedException, 'Требуется авторизация' if session[:login].nil?
  end
end
