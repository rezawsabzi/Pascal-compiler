var
   x:integer;
   y:integer;
begin
  y:=8;

  if(y>5)then begin
    y:=5;
    for x:=y downto 1 do begin
         writeln(x);
    end;

  end;

  readln;
end.   




