# frozen_string_literal: true

# Controller for login
class LoginsController < ApplicationController
  before_action :authenticate_user!, only: [:destroy]
  skip_before_action :verify_authenticity_token

  def show
    render json: { result: session[:login].present?, login: session[:login] }
  end

  def create
    LoginService.new(params, session).call

    render json: { result: true, message: "Успешно" }
  rescue StandardError
    render json: { result: false, message: "Неверный пароль" }
  end

  def destroy
    session.delete(:login)
    render json: { result: true, message: "Вы вышли" }
  end
end
