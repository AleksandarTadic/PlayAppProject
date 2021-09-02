from flask import Flask
from utils.db import mysql

from blueprints.account import account_blueprint
from blueprints.movie import movie_blueprint
from blueprints.video import video_blueprint

app = Flask(__name__, static_url_path="")

app.config["MYSQL_DATABASE_USER"] = "root" 
app.config["MYSQL_DATABASE_PASSWORD"] = "root" 
app.config["MYSQL_DATABASE_DB"] = "playAPP" 
app.config["SECRET_KEY"] = "ta WJtrz29$"

mysql.init_app(app) 

app.register_blueprint(account_blueprint, url_prefix="/api")
app.register_blueprint(movie_blueprint, url_prefix="/api")
app.register_blueprint(video_blueprint, url_prefix="/api")


@app.route("/")
def main():
    return "hello world!"


if __name__ == "__main__":
    app.run("0.0.0.0", 5000, threaded=True, debug=False)