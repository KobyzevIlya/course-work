# frozen_string_literal: true

# Controller for login
class LoginsController < ApplicationController
  before_action :authenticate_user!, only: [:destroy]
  skip_before_action :verify_authenticity_token

  rescue_from(IncorrectLoginException) { render json: { result: false, message: "Неверный логин" } }
  rescue_from(IncorrectPasswordException) { render json: { result: false, message: "Неверный пароль" } }
  rescue_from(LoginExistsException) { render json: { result: false, message: "Логин существует" } }

  def show
    render json: { result: session[:login].present?, message: session[:login].present? ? session[:login] : "Вход не выполнен" }
  end

  def create
    LoginService.new(params, session).call

    render json: { result: true, message: "Успешно" }
  end

  def destroy
    session.delete(:login)
    render json: { result: true, message: "Вы вышли" }
  end

  def register
    users_controller = UsersController.new
    users_controller.request = request
    users_controller.response = response
    users_controller.params = params

    render json: users_controller.create
  end
end
