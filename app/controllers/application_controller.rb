class ApplicationController < ActionController::Base
  before_action :authenticate_user!

  def authenticate_user!
    redirect_to :login if session[:login].nil?
  end

  def check_authorize!
    raise NotAuthorizedException, 'Требуется авторизация' if session[:login].nil?
  end
end
