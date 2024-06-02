json.extract! news, :id, :title, :image, :publisher, :text, :created_at, :updated_at, :link
json.url news_url(news, format: :json)
