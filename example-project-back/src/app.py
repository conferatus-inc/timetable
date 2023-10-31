from fastapi import FastAPI

app = FastAPI()

@app.get('/api/example')
def root():
    return {'message': 'Hello World'}
