# frozen_string_literal: true

# Service to login
class LoginService
  attr_reader :params, :session

  def initialize(params, session)
    @params = params
    @session = session
  end

  def call
    check_login
    check_password
    modify_session
    message
  end

  private

  def check_login
    @user = User.find_by(login: @params[:login])
    raise IncorrectLoginException, 'Неверный логин' unless @user
  end


  def check_password
    raise IncorrectPasswordException, 'Неверный пароль' if @params[:password] != @user.password
  end

  def modify_session
    @session[:login] = @params[:login]
  end

  def message
    "Добро пожаловать, #{@params[:login]}"
  end
end
