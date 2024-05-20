require 'rails_helper'

RSpec.describe "users/index", type: :view do
  before(:each) do
    assign(:users, [
      User.create!(
        login: "Login"
      ),
      User.create!(
        login: "Login"
      )
    ])
  end

  it "renders a list of users" do
    render
    assert_select "tr>td", text: "Login".to_s, count: 2
  end
end
