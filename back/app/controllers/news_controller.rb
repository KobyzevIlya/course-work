class NewsController < ApplicationController
  skip_before_action :verify_authenticity_token
  before_action :set_news, only: %i[ show edit update destroy ]
  before_action :check_authorize!

  rescue_from(NotAuthorizedException) { |e| render json: e.body, status: 401 }

  def likes_count
    @news = News.find(params[:id])

    render json: { result: true, message: @news.news_users.count }
  end

  def check_like
    @user = User.find_by(login: session[:login])
    @news = News.find(params[:id])

    render json: { result: is_liked, message: "Без сообщения" }
  end

  def like
    @user = User.find_by(login: session[:login])
    @news = News.find(params[:id])

    if is_liked
      @news.users.delete(@user)
      render json: { result: true, message: "Лайк удален" }
    else
      @news.users << @user
      render json: { result: true, message: "Лайк добавлен" }
    end
  end

  # GET /news or /news.json
  def index
    if params[:title].present?
      @news = News.where('title LIKE ?', "%#{params[:title]}%")
    else
      @news = News.all
    end
  end

  # GET /news/1 or /news/1.json
  def show
  end

  # GET /news/new
  def new
    @news = News.new
  end

  # GET /news/1/edit
  def edit
  end

  # POST /news or /news.json
  def create
    @news = News.new(news_params)

    respond_to do |format|
      if @news.save
        format.html { redirect_to news_url(@news), notice: "News was successfully created." }
        format.json { render :show, status: :created, location: @news }
      else
        format.html { render :new, status: :unprocessable_entity }
        format.json { render json: @news.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /news/1 or /news/1.json
  def update
    respond_to do |format|
      if @news.update(news_params)
        format.html { redirect_to news_url(@news), notice: "News was successfully updated." }
        format.json { render :show, status: :ok, location: @news }
      else
        format.html { render :edit, status: :unprocessable_entity }
        format.json { render json: @news.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /news/1 or /news/1.json
  def destroy
    @news.destroy!

    respond_to do |format|
      format.html { redirect_to news_index_url, notice: "News was successfully destroyed." }
      format.json { head :no_content }
    end
  end

  private
    def is_liked
      NewsUser.find_by(user_id: @user.id, news_id: @news.id).present?
    end

    # Use callbacks to share common setup or constraints between actions.
    def set_news
      @news = News.find(params[:id])
    end

    # Only allow a list of trusted parameters through.
    def news_params
      params.require(:news).permit(:title, :image, :publisher, :text, :link)
    end
end
