docker build --build-arg APP_ENV=prod -t razear/mega-electric-app:latest -t razear/mega-electric-app:$SHA -f Dockerfile .


docker push razear/mega-electric-app:latest
docker push razear/mega-electric-app:$SHA

fandogh service deploy --image razear/mega-electric-app --version latest --name mega-electric-app  -d
