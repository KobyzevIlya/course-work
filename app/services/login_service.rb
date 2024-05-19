# frozen_string_literal: true

# Service to login
class LoginService
  attr_reader :params, :session

  def initialize(params, session)
    @params = params
    @session = session
  end

  def call
    check_password
    modify_session
    message
  rescue StandardError
    'Неверный пароль'
  end

  private

  def check_password
    raise if @params[:password] != '123'
  end

  def modify_session
    @session[:login] = @params[:login]
  end

  def message
    "Добро пожаловать, #{@params[:login]}"
  end
end
