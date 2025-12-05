// 'use client';

// import { Canvas } from '@react-three/fiber';
// import { OrbitControls } from '@react-three/drei';
// import { Suspense } from 'react';

// function FloatingSphere() {
//   return (
//     <mesh>
//       <sphereGeometry args={[1.2, 64, 64]} />
//       <meshStandardMaterial
//         color="#FFFFFF"
//         metalness={0.4}
//         roughness={0.2}
//       />
//     </mesh>
//   );
// }

// export function HeroOrbitModel() {
//   return (
//     <div className="relative h-64 w-full rounded-[24px] bg-[#F5F5F7]">
//       <Canvas
//         camera={{ position: [0, 0, 4], fov: 45 }}
//         gl={{ antialias: true }}
//       >
//         <Suspense fallback={null}>
//           <ambientLight intensity={0.5} />
//           <directionalLight position={[3, 4, 5]} intensity={1} />
//           <FloatingSphere />
//           <OrbitControls
//             enablePan={false}
//             enableZoom={false}
//             autoRotate
//             autoRotateSpeed={0.4}
//           />
//         </Suspense>
//       </Canvas>
//     </div>
//   );
// }

import { Canvas, useFrame } from '@react-three/fiber';
import { OrbitControls, Float, MeshTransmissionMaterial, Environment, useTexture, Text3D, Center, Sparkles } from '@react-three/drei';
import { Suspense, useRef, useMemo } from 'react';
import * as THREE from 'three';

// Carrito de compras ultra detallado
function PremiumShoppingCart() {
  const cartRef = useRef<THREE.Group>(null);
  
  useFrame((state) => {
    if (cartRef.current) {
      cartRef.current.rotation.y = Math.sin(state.clock.elapsedTime * 0.3) * 0.15;
      cartRef.current.position.y = Math.sin(state.clock.elapsedTime * 0.5) * 0.1;
    }
  });

  return (
    <group ref={cartRef} scale={1.2}>
      {/* Base del carrito - estructura de malla */}
      <mesh position={[0, 0, 0]} castShadow>
        <boxGeometry args={[1.4, 1, 1.2]} />
        <meshStandardMaterial 
          color="#2563eb" 
          metalness={0.9}
          roughness={0.1}
          envMapIntensity={1}
        />
      </mesh>

      {/* Bordes superiores con brillo */}
      <mesh position={[0, 0.51, 0]} castShadow>
        <boxGeometry args={[1.42, 0.08, 1.22]} />
        <meshStandardMaterial 
          color="#3b82f6" 
          metalness={1}
          roughness={0.05}
          emissive="#3b82f6"
          emissiveIntensity={0.3}
        />
      </mesh>

      {/* Panel frontal inclinado */}
      <mesh position={[0, -0.2, 0.62]} rotation={[0.2, 0, 0]} castShadow>
        <boxGeometry args={[1.4, 0.6, 0.05]} />
        <meshStandardMaterial 
          color="#1e40af" 
          metalness={0.8}
          roughness={0.15}
        />
      </mesh>

      {/* Rejilla decorativa del frente */}
      {Array.from({ length: 8 }).map((_, i) => (
        <mesh key={i} position={[-0.6 + i * 0.17, -0.2, 0.64]} castShadow>
          <boxGeometry args={[0.03, 0.5, 0.02]} />
          <meshStandardMaterial color="#60a5fa" metalness={1} roughness={0.1} />
        </mesh>
      ))}

      {/* Ruedas premium con detalles */}
      {[
        [-0.5, -0.7, 0.5],
        [0.5, -0.7, 0.5],
        [-0.5, -0.7, -0.5],
        [0.5, -0.7, -0.5]
      ].map((pos, i) => (
        <group key={i} position={pos as [number, number, number]}>
          {/* Llanta */}
          <mesh rotation={[Math.PI / 2, 0, 0]} castShadow>
            <torusGeometry args={[0.18, 0.08, 16, 32]} />
            <meshStandardMaterial color="#1f2937" metalness={0.3} roughness={0.7} />
          </mesh>
          {/* Centro de la rueda */}
          <mesh rotation={[Math.PI / 2, 0, 0]} castShadow>
            <cylinderGeometry args={[0.12, 0.12, 0.06, 32]} />
            <meshStandardMaterial color="#6b7280" metalness={0.9} roughness={0.2} />
          </mesh>
          {/* Rin brillante */}
          <mesh rotation={[Math.PI / 2, 0, 0]}>
            <cylinderGeometry args={[0.08, 0.08, 0.08, 32]} />
            <meshStandardMaterial color="#e5e7eb" metalness={1} roughness={0.05} />
          </mesh>
        </group>
      ))}

      {/* Manija ergonómica */}
      <group position={[0, 0.6, -0.6]}>
        <mesh position={[0, 0, 0]} rotation={[0.3, 0, 0]} castShadow>
          <boxGeometry args={[1.2, 0.08, 0.08]} />
          <meshStandardMaterial color="#1e40af" metalness={0.9} roughness={0.1} />
        </mesh>
        <mesh position={[-0.58, -0.3, 0]} castShadow>
          <boxGeometry args={[0.06, 0.6, 0.06]} />
          <meshStandardMaterial color="#1e40af" metalness={0.9} roughness={0.1} />
        </mesh>
        <mesh position={[0.58, -0.3, 0]} castShadow>
          <boxGeometry args={[0.06, 0.6, 0.06]} />
          <meshStandardMaterial color="#1e40af" metalness={0.9} roughness={0.1} />
        </mesh>
        {/* Grips de goma */}
        <mesh position={[0, 0, 0]} rotation={[0.3, 0, 0]}>
          <cylinderGeometry args={[0.05, 0.05, 1.1, 32]} />
          <meshStandardMaterial color="#374151" roughness={0.9} />
        </mesh>
      </group>

      {/* Logo en el carrito */}
      <mesh position={[0, 0.1, 0.61]}>
        <circleGeometry args={[0.15, 32]} />
        <meshStandardMaterial 
          color="#60a5fa" 
          metalness={1} 
          roughness={0.05}
          emissive="#60a5fa"
          emissiveIntensity={0.5}
        />
      </mesh>
    </group>
  );
}

// Producto 3D realista: Caja de producto premium
function ProductBox({ position, color, label }: { position: [number, number, number], color: string, label: string }) {
  const meshRef = useRef<THREE.Group>(null);
  
  useFrame((state) => {
    if (meshRef.current) {
      const time = state.clock.elapsedTime;
      const radius = 3;
      const speed = 0.3;
      const offset = position[0] * 2;
      
      meshRef.current.position.x = Math.cos(time * speed + offset) * radius;
      meshRef.current.position.y = position[1] + Math.sin(time * 0.5 + offset) * 0.3;
      meshRef.current.position.z = Math.sin(time * speed + offset) * radius;
      
      meshRef.current.rotation.y = time * 0.5 + offset;
      meshRef.current.rotation.x = Math.sin(time * 0.3) * 0.2;
    }
  });

  return (
    <Float speed={1.5} rotationIntensity={0.3} floatIntensity={0.5}>
      <group ref={meshRef} position={position}>
        {/* Caja principal */}
        <mesh castShadow>
          <boxGeometry args={[0.6, 0.6, 0.6]} />
          <meshStandardMaterial 
            color={color}
            metalness={0.2}
            roughness={0.3}
            envMapIntensity={0.8}
          />
        </mesh>
        
        {/* Etiqueta frontal */}
        <mesh position={[0, 0, 0.301]} castShadow>
          <boxGeometry args={[0.5, 0.4, 0.02]} />
          <meshStandardMaterial 
            color="#ffffff"
            metalness={0}
            roughness={0.4}
          />
        </mesh>

        {/* Detalles de la etiqueta */}
        <mesh position={[0, 0.1, 0.32]}>
          <boxGeometry args={[0.4, 0.08, 0.01]} />
          <meshStandardMaterial color={color} />
        </mesh>
        <mesh position={[0, -0.05, 0.32]}>
          <boxGeometry args={[0.35, 0.03, 0.01]} />
          <meshStandardMaterial color="#9ca3af" />
        </mesh>
        <mesh position={[0, -0.12, 0.32]}>
          <boxGeometry args={[0.3, 0.03, 0.01]} />
          <meshStandardMaterial color="#d1d5db" />
        </mesh>

        {/* Cinta de embalaje */}
        <mesh position={[0, 0.301, 0]} rotation={[Math.PI / 2, 0, 0]}>
          <boxGeometry args={[0.62, 0.62, 0.02]} />
          <meshStandardMaterial 
            color="#fbbf24"
            metalness={0.6}
            roughness={0.2}
            transparent
            opacity={0.9}
          />
        </mesh>

        {/* Brillo sutil */}
        <pointLight position={[0, 0, 0.4]} intensity={0.5} color={color} distance={1} />
      </group>
    </Float>
  );
}

// Tarjeta de crédito 3D premium
function CreditCard({ position }: { position: [number, number, number] }) {
  const cardRef = useRef<THREE.Group>(null);
  
  useFrame((state) => {
    if (cardRef.current) {
      const time = state.clock.elapsedTime;
      const radius = 3.5;
      const speed = 0.25;
      
      cardRef.current.position.x = Math.cos(time * speed + Math.PI) * radius;
      cardRef.current.position.y = position[1] + Math.sin(time * 0.4) * 0.4;
      cardRef.current.position.z = Math.sin(time * speed + Math.PI) * radius;
      
      cardRef.current.rotation.y = time * 0.6;
      cardRef.current.rotation.z = Math.sin(time * 0.5) * 0.1;
    }
  });

  return (
    <Float speed={2} rotationIntensity={0.2} floatIntensity={0.4}>
      <group ref={cardRef} position={position}>
        {/* Tarjeta base */}
        <mesh castShadow>
          <boxGeometry args={[1.2, 0.75, 0.05]} />
          <meshStandardMaterial 
            color="#1f2937"
            metalness={0.9}
            roughness={0.1}
            envMapIntensity={1.5}
          />
        </mesh>

        {/* Gradiente holográfico simulado */}
        <mesh position={[0, 0, 0.026]}>
          <boxGeometry args={[1.18, 0.73, 0.01]} />
          <meshStandardMaterial 
            color="#8b5cf6"
            metalness={1}
            roughness={0}
            emissive="#8b5cf6"
            emissiveIntensity={0.3}
            transparent
            opacity={0.4}
          />
        </mesh>

        {/* Chip dorado */}
        <mesh position={[-0.3, 0.15, 0.03]} castShadow>
          <boxGeometry args={[0.15, 0.12, 0.02]} />
          <meshStandardMaterial 
            color="#fbbf24"
            metalness={1}
            roughness={0.1}
          />
        </mesh>

        {/* Detalles del chip */}
        <mesh position={[-0.3, 0.15, 0.04]}>
          <boxGeometry args={[0.12, 0.09, 0.01]} />
          <meshStandardMaterial color="#f59e0b" metalness={1} />
        </mesh>

        {/* Banda magnética */}
        <mesh position={[0, 0.3, -0.026]}>
          <boxGeometry args={[1.2, 0.12, 0.005]} />
          <meshStandardMaterial color="#111827" />
        </mesh>

        {/* Números de la tarjeta (simulados) */}
        {[0, 1, 2, 3].map((i) => (
          <mesh key={i} position={[-0.4 + i * 0.25, -0.1, 0.026]}>
            <boxGeometry args={[0.15, 0.08, 0.01]} />
            <meshStandardMaterial 
              color="#e5e7eb"
              metalness={0.8}
              roughness={0.2}
            />
          </mesh>
        ))}

        {/* Brillo de la tarjeta */}
        <pointLight position={[0, 0, 0.5]} intensity={0.8} color="#8b5cf6" distance={1.5} />
      </group>
    </Float>
  );
}

// Smartphone 3D moderno
function Smartphone({ position }: { position: [number, number, number] }) {
  const phoneRef = useRef<THREE.Group>(null);
  
  useFrame((state) => {
    if (phoneRef.current) {
      const time = state.clock.elapsedTime;
      const radius = 3.2;
      const speed = 0.35;
      const offset = Math.PI / 2;
      
      phoneRef.current.position.x = Math.cos(time * speed + offset) * radius;
      phoneRef.current.position.y = position[1] + Math.sin(time * 0.6) * 0.35;
      phoneRef.current.position.z = Math.sin(time * speed + offset) * radius;
      
      phoneRef.current.rotation.y = time * 0.7;
      phoneRef.current.rotation.x = Math.sin(time * 0.4) * 0.15;
    }
  });

  return (
    <Float speed={1.8} rotationIntensity={0.25} floatIntensity={0.45}>
      <group ref={phoneRef} position={position}>
        {/* Cuerpo del teléfono */}
        <mesh castShadow>
          <boxGeometry args={[0.4, 0.8, 0.08]} />
          <meshStandardMaterial 
            color="#0f172a"
            metalness={0.95}
            roughness={0.05}
            envMapIntensity={2}
          />
        </mesh>

        {/* Pantalla */}
        <mesh position={[0, 0, 0.041]}>
          <boxGeometry args={[0.36, 0.72, 0.01]} />
          <meshStandardMaterial 
            color="#1e293b"
            metalness={0.8}
            roughness={0.1}
            emissive="#3b82f6"
            emissiveIntensity={0.4}
          />
        </mesh>

        {/* Notch */}
        <mesh position={[0, 0.32, 0.042]}>
          <boxGeometry args={[0.12, 0.03, 0.01]} />
          <meshStandardMaterial color="#0f172a" />
        </mesh>

        {/* Cámara trasera */}
        <group position={[-0.12, 0.28, -0.041]}>
          <mesh castShadow>
            <boxGeometry args={[0.12, 0.12, 0.02]} />
            <meshStandardMaterial color="#1f2937" metalness={0.8} roughness={0.2} />
          </mesh>
          <mesh position={[-0.03, 0.03, 0.01]}>
            <cylinderGeometry args={[0.025, 0.025, 0.015, 32]} />
            <meshStandardMaterial color="#374151" metalness={1} roughness={0.1} />
          </mesh>
          <mesh position={[0.03, 0.03, 0.01]}>
            <cylinderGeometry args={[0.025, 0.025, 0.015, 32]} />
            <meshStandardMaterial color="#374151" metalness={1} roughness={0.1} />
          </mesh>
        </group>

        {/* Botones laterales */}
        <mesh position={[0.21, 0.15, 0]}>
          <boxGeometry args={[0.02, 0.08, 0.06]} />
          <meshStandardMaterial color="#475569" metalness={0.9} />
        </mesh>

        {/* Brillo de pantalla */}
        <pointLight position={[0, 0, 0.3]} intensity={0.6} color="#3b82f6" distance={1} />
      </group>
    </Float>
  );
}

// Partículas premium de fondo
function PremiumParticles() {
  const particlesRef = useRef<THREE.Points>(null);
  
  const { positions, colors } = useMemo(() => {
    const count = 150;
    const pos = new Float32Array(count * 3);
    const cols = new Float32Array(count * 3);
    
    for (let i = 0; i < count; i++) {
      const i3 = i * 3;
      const theta = Math.random() * Math.PI * 2;
      const phi = Math.random() * Math.PI;
      const radius = 5 + Math.random() * 3;
      
      pos[i3] = radius * Math.sin(phi) * Math.cos(theta);
      pos[i3 + 1] = radius * Math.sin(phi) * Math.sin(theta);
      pos[i3 + 2] = radius * Math.cos(phi);
      
      const color = new THREE.Color();
      color.setHSL(0.6 + Math.random() * 0.2, 0.8, 0.6);
      cols[i3] = color.r;
      cols[i3 + 1] = color.g;
      cols[i3 + 2] = color.b;
    }
    
    return { positions: pos, colors: cols };
  }, []);
  
  useFrame((state) => {
    if (particlesRef.current) {
      particlesRef.current.rotation.y = state.clock.elapsedTime * 0.03;
      particlesRef.current.rotation.x = Math.sin(state.clock.elapsedTime * 0.02) * 0.1;
    }
  });
  
  return (
    <points ref={particlesRef}>
      <bufferGeometry>
        <bufferAttribute
          attach="attributes-position"
          count={positions.length / 3}
          array={positions}
          itemSize={3}
        />
        <bufferAttribute
          attach="attributes-color"
          count={colors.length / 3}
          array={colors}
          itemSize={3}
        />
      </bufferGeometry>
      <pointsMaterial 
        size={0.04} 
        vertexColors
        transparent
        opacity={0.8}
        sizeAttenuation
        blending={THREE.AdditiveBlending}
      />
    </points>
  );
}

export function HeroOrbitModel() {
  return (
    <div className="relative h-64 w-full rounded-[24px] bg-gradient-to-br from-slate-50 via-blue-50 to-slate-100 overflow-hidden shadow-2xl border border-white/20">
      {/* Overlay con efecto glassmorphism */}
      <div className="absolute inset-0 bg-gradient-to-t from-black/10 via-transparent to-white/20 pointer-events-none z-10" />
      
      {/* Animated gradient background */}
      <div className="absolute inset-0 opacity-30">
        <div className="absolute inset-0 bg-gradient-to-r from-blue-400/20 via-purple-400/20 to-pink-400/20 animate-gradient" />
      </div>
      
      <Canvas
        camera={{ position: [0, 1.5, 8], fov: 45 }}
        gl={{ 
          antialias: true, 
          alpha: true,
          toneMapping: THREE.ACESFilmicToneMapping,
          toneMappingExposure: 1.2
        }}
        shadows
      >
        <Suspense fallback={null}>
          {/* Iluminación profesional tipo estudio */}
          <ambientLight intensity={0.4} />
          <directionalLight 
            position={[5, 8, 5]} 
            intensity={1.5} 
            castShadow
            shadow-mapSize-width={2048}
            shadow-mapSize-height={2048}
          />
          <directionalLight position={[-5, 5, -5]} intensity={0.8} />
          <spotLight 
            position={[0, 10, 0]} 
            angle={0.5} 
            penumbra={1} 
            intensity={1.2}
            castShadow
            color="#3b82f6"
          />
          <pointLight position={[0, 0, 5]} intensity={0.8} color="#60a5fa" />
          
          {/* Environment map para reflejos realistas */}
          <Environment preset="city" />
          
          {/* Sparkles mágicos */}
          <Sparkles 
            count={50}
            scale={10}
            size={2}
            speed={0.3}
            opacity={0.6}
            color="#3b82f6"
          />
          
          {/* Partículas de fondo */}
          <PremiumParticles />
          
          {/* Carrito central premium */}
          <PremiumShoppingCart />
          
          {/* Productos realistas orbitando */}
          <ProductBox position={[2, 0.5, 0]} color="#ef4444" label="Tech" />
          <ProductBox position={[-2, 0.8, 1]} color="#10b981" label="Fashion" />
          <ProductBox position={[1.5, -0.3, -1.5]} color="#f59e0b" label="Home" />
          <ProductBox position={[-1.8, 1.2, -1]} color="#8b5cf6" label="Sports" />
          
          {/* Tarjeta de crédito */}
          <CreditCard position={[0, 1.5, 2]} />
          
          {/* Smartphone */}
          <Smartphone position={[2.5, 0, 1.5]} />
          
          {/* Controles suaves */}
          <OrbitControls
            enablePan={false}
            enableZoom={false}
            autoRotate
            autoRotateSpeed={0.4}
            minPolarAngle={Math.PI / 3.5}
            maxPolarAngle={Math.PI / 1.8}
            maxDistance={10}
            minDistance={6}
          />
        </Suspense>
      </Canvas>
      
      {/* Badge premium con glassmorphism */}
      <div className="absolute bottom-4 left-4 bg-white/40 backdrop-blur-xl px-4 py-2 rounded-2xl text-sm font-semibold text-slate-800 shadow-xl border border-white/30 z-20 flex items-center gap-2">
        <span className="text-yellow-500">✨</span>
        <span>Compra con confianza</span>
      </div>
      
      {/* Indicador de interactividad */}
      <div className="absolute bottom-4 right-4 bg-blue-500/40 backdrop-blur-xl px-3 py-1.5 rounded-full text-xs font-medium text-white shadow-lg border border-blue-400/30 z-20 animate-pulse">
        Arrastra para rotar
      </div>
    </div>
  );
}