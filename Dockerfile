FROM clojure:alpine

COPY . /app
WORKDIR /app

CMD ["lein", "run"]
