class CreateNewsUsers < ActiveRecord::Migration[6.1]
  def change
    create_table :news_users do |t|
      t.references :news, null: false, foreign_key: true
      t.references :user, null: false, foreign_key: true

      t.timestamps
    end

    add_index :news_users, [:news_id, :user_id], unique: true
  end
end
