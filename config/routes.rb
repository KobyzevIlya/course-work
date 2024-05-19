Rails.application.routes.draw do
  resources :users
  resources :news
  resource :login, only: [:show, :create, :destroy]
  # For details on the DSL available within this file, see https://guides.rubyonrails.org/routing.html
end
