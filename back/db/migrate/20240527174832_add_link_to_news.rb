class AddLinkToNews < ActiveRecord::Migration[6.1]
  def change
    add_column :news, :link, :string
  end
end
