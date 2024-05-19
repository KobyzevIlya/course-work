# frozen_string_literal: true

# Controller for login
class LoginsController < ApplicationController
  before_action :authenticate_user!, only: [:destroy]

  def show; end

  def create
    redirect_to :login, notice: LoginService.new(params, session).call
  end

  def destroy
    session.delete(:login)
    redirect_to :login, notice: 'Вы вышли'
  end
end
