from typing import AnyStr, List

class _unicode: ...

def commonprefix(list: List[AnyStr]) -> AnyStr: ...
def exists(path: unicode) -> bool: ...
def getatime(path: unicode) -> float: ...
def getmtime(path: unicode) -> float: ...
def getctime(path: unicode) -> float: ...
def getsize(path: unicode) -> int: ...
def isfile(path: unicode) -> bool: ...
def isdir(path: unicode) -> bool: ...
