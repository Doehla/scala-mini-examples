# scala-mini-examples
A random collection of small scala projects.

## apiWithHttp4s
This is some sample code where we leverage the [Http4s](https://github.com/http4s/http4s) to set up a simple API.

Project can be launced by running `apiWithHttp4s /run` from within `sbt`.

The code looks for a JSON object to be included at the `hello` endpoint with just some name included. This can be posted as such to the endpoint:
```bash
curl -i http://127.0.0.1:8080/hello -H 'Content-Type: application/json' -d '{"name":"some name!"}'
```
