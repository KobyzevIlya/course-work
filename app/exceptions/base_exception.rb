# frozen_string_literal: true

# Exception with base response
class BaseException < StandardError
  def body
    { result: false, error: message }
  end
end
