var
   i, j:integer;
begin


  for i := 1 to 100 do
  begin
   if ((i mod 4 = 0) and (i mod 3 <> 0)) then begin
    writeln(i);
   end;
  end;
end.

