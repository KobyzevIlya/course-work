Rails.application.routes.draw do
  resources :users
  resources :news do
    member do
      get 'likes_count'
      get 'check_like'
      get 'like'
    end
  end
  resource :login, only: [:show, :create, :destroy]
  resource :login do
    post 'register'
  end
  # For details on the DSL available within this file, see https://guides.rubyonrails.org/routing.html
end
