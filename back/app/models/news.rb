class News < ApplicationRecord
  has_many :news_users
  has_many :users, through: :news_users
end
