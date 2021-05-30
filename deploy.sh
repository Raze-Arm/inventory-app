

docker push razear/mega-electric-app:latest
docker push razear/mega-electric-app:$SHA

#fandogh service deploy --env SPRING_PROFILES_ACTIVE=prod --env JWT_SECRET_KEY=$JWT_SECRET_KEY --env JWT_TOKEN_EXPIRE=${JWT_TOKEN_EXPIRE} --env JWT_TOKEN_PREFIX=${JWT_TOKEN_PREFIX} --env MYSQL_USERNAME=${MYSQL_USERNAME} --env MYSQL_PASSWORD=${MYSQL_PASSWORD} --env MYSQL_HOST=${MYSQL_HOST} --env MYSQL_DB_NAME=${MYSQL_DB_NAME} --image razear/mega-electric-app --version $SHA --name mega-electric-app  -p 8080  -d
fandogh  secret create --name SECRET_NAME -t environment-secret -f  SPRING_PROFILES_ACTIVE=prod
fandogh  secret create --name SECRET_NAME -t environment-secret -f  JWT_SECRET_KEY=$JWT_SECRET_KEY
fandogh  secret create --name SECRET_NAME -t environment-secret -f  JWT_TOKEN_EXPIRE=$JWT_TOKEN_EXPIRE
fandogh  secret create --name SECRET_NAME -t environment-secret -f  JWT_TOKEN_PREFIX=$JWT_TOKEN_PREFIX
fandogh  secret create --name SECRET_NAME -t environment-secret -f  MYSQL_USERNAME=$MYSQL_USERNAME
fandogh  secret create --name SECRET_NAME -t environment-secret -f  MYSQL_PASSWORD=$MYSQL_PASSWORD
fandogh  secret create --name SECRET_NAME -t environment-secret -f  MYSQL_HOST=$MYSQL_HOST
fandogh  secret create --name SECRET_NAME -t environment-secret -f  MYSQL_DB_NAME=$MYSQL_DB_NAME

fandogh service apply -f app-deployment.yml  \
                 -p SHA=$SHA
#                 -p SPRING_PROFILES_ACTIVE=prod \
#                 -p JWT_SECRET_KEY=$JWT_SECRET_KEY \
#                 -p JWT_TOKEN_EXPIRE=$JWT_TOKEN_EXPIRE -p JWT_TOKEN_PREFIX=$JWT_TOKEN_PREFIX \
#                 -p MYSQL_USERNAME=$MYSQL_USERNAME -p MYSQL_PASSWORD=$MYSQL_PASSWORD \
#                 -p MYSQL_HOST=$MYSQL_HOST -p MYSQL_DB_NAME=$MYSQL_DB_NAME