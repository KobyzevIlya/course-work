require 'rails_helper'

RSpec.describe "news/index", type: :view do
  before(:each) do
    assign(:news, [
      News.create!(
        title: "Title",
        image: "Image",
        publisher: "Publisher",
        text: "Text"
      ),
      News.create!(
        title: "Title",
        image: "Image",
        publisher: "Publisher",
        text: "Text"
      )
    ])
  end

  it "renders a list of news" do
    render
    assert_select "tr>td", text: "Title".to_s, count: 2
    assert_select "tr>td", text: "Image".to_s, count: 2
    assert_select "tr>td", text: "Publisher".to_s, count: 2
    assert_select "tr>td", text: "Text".to_s, count: 2
  end
end
