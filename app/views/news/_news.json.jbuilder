json.extract! news, :id, :title, :image, :publisher, :text, :created_at, :updated_at
json.url news_url(news, format: :json)
