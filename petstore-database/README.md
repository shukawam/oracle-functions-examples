# petstore-database

## How to build

set your local env.

```bash
export NAMESPACE=<object storage namespace>
export BUCKET_NAME=<bucket name>
export DB_USER=<db user>
export DB_PASSWORD=<db password>
export DB_URL=<db connection string>
```

create app.

```bash
fn create app petstore
```

build and push docker image.

```bash
fn deploy --app petstore  \
--build-arg namespace=$NAMESPACE \
--build-arg bucket_name=$BUCKET_NAME \
--build-arg db_user=$DB_USER \
--build-arg db_passwordDB_PASSWORD \
--build-arg db_url=$DB_URL
```
