require 'rails_helper'

RSpec.describe "news/edit", type: :view do
  before(:each) do
    @news = assign(:news, News.create!(
      title: "MyString",
      image: "MyString",
      publisher: "MyString",
      text: "MyString"
    ))
  end

  it "renders the edit news form" do
    render

    assert_select "form[action=?][method=?]", news_path(@news), "post" do

      assert_select "input[name=?]", "news[title]"

      assert_select "input[name=?]", "news[image]"

      assert_select "input[name=?]", "news[publisher]"

      assert_select "input[name=?]", "news[text]"
    end
  end
end
