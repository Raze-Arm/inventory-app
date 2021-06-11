
docker push razear/mega-electric-app:latest
docker push razear/mega-electric-app:$SHA

fandogh service apply -f app-deployment.yml  \
                 -p SHA=$SHA -p ALLOWED_ORIGINS=$ALLOWED_ORIGINS -p SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE \
                 -p JWT_TOKEN=$JWT_TOKEN  -p JWT_TOKEN_EXPIRE=$JWT_TOKEN_EXPIRE -p JWT_TOKEN_PREFIX=$JWT_TOKEN_PREFIX \
                 -p MYSQL_USERNAME=$MYSQL_USERNAME -p MYSQL_PASSWORD=$MYSQL_PASSWORD -p MYSQL_HOST=$MYSQL_HOST  \
                 -p MYSQL_DB_NAME=$MYSQL_DB_NAME


