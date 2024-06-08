var
   x:integer;
   y , i:integer;
begin
  y:=8;
  x:=5;
  if ( x + y = 13) then begin

    while(x <> 50) do begin
        if ((x mod 5 = 0) and (x mod 2 <> 0)) then begin
            for i := 1 to x do begin
                write("*");
            end;
            writeln();
        end;
        x := x + 1;
    end;
  end;
end.











