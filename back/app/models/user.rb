class User < ApplicationRecord
  has_many :news_users
  has_many :news, through: :news_users
end
